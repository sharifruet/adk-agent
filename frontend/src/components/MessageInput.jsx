import { useEffect, useRef, useState } from 'react';
import { useConversation } from '../context/ConversationContext';
import { useLocale } from '../context/LocaleContext';
import './MessageInput.css';

const MAX_HEIGHT = 168; // about five lines of Bangla

const SendIcon = () => (
  <svg
    viewBox="0 0 20 20"
    width="18"
    height="18"
    aria-hidden="true"
    fill="none"
    stroke="currentColor"
    strokeWidth="2"
    strokeLinecap="round"
    strokeLinejoin="round"
  >
    <path d="M10 16V4M4.5 9.5 10 4l5.5 5.5" />
  </svg>
);

const MessageInput = () => {
  const [input, setInput] = useState('');
  const textareaRef = useRef(null);
  const { sendMessage, isLoading, requiresLeadCapture } = useConversation();
  const { t } = useLocale();

  const locked = requiresLeadCapture;
  const canSend = input.trim().length > 0 && !isLoading && !locked;

  // Grow with the text, up to MAX_HEIGHT, then scroll.
  useEffect(() => {
    const el = textareaRef.current;
    if (!el) return;
    el.style.height = 'auto';
    el.style.height = `${Math.min(el.scrollHeight, MAX_HEIGHT)}px`;
  }, [input]);

  const submit = async () => {
    if (!canSend) return;
    const question = input.trim();
    setInput('');
    await sendMessage(question);
    textareaRef.current?.focus();
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    submit();
  };

  const handleKeyDown = (e) => {
    // Enter sends, Shift+Enter breaks the line. Ignore Enter while an IME is
    // still composing (Android and Google Input Tools Bangla keyboards).
    if (e.key === 'Enter' && !e.shiftKey && !e.nativeEvent.isComposing) {
      e.preventDefault();
      submit();
    }
  };

  return (
    <footer className="composer">
      <form className="composer-inner" onSubmit={handleSubmit}>
        <label htmlFor="composer-input" className="sr-only">
          {t.placeholder}
        </label>
        <div className={`composer-field${locked ? ' is-locked' : ''}`}>
          <textarea
            id="composer-input"
            ref={textareaRef}
            rows={1}
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={handleKeyDown}
            placeholder={locked ? t.placeholderLocked : t.placeholder}
            disabled={locked}
            autoComplete="off"
            enterKeyHint="send"
          />
          <button
            type="submit"
            className="composer-send"
            disabled={!canSend}
            aria-label={t.send}
            title={t.send}
          >
            <SendIcon />
          </button>
        </div>
        <p className="composer-note">{t.disclaimer}</p>
      </form>
    </footer>
  );
};

export default MessageInput;
