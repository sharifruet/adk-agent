import { useEffect, useRef } from 'react';
import { ConversationProvider, useConversation } from '../context/ConversationContext';
import { LocaleProvider, useLocale } from '../context/LocaleContext';
import MessageList from './MessageList';
import MessageInput from './MessageInput';
import LeadCaptureForm from './LeadCaptureForm';
import TypingIndicator from './TypingIndicator';
import logo from '../assets/logo.png';
import './ChatInterface.css';

const PlusIcon = () => (
  <svg
    viewBox="0 0 20 20"
    width="16"
    height="16"
    aria-hidden="true"
    fill="none"
    stroke="currentColor"
    strokeWidth="1.8"
    strokeLinecap="round"
  >
    <path d="M10 4.5v11M4.5 10h11" />
  </svg>
);

const ChatInterfaceContent = () => {
  const { messages, isLoading, requiresLeadCapture, clearConversation } = useConversation();
  const { locale, setLocale, t } = useLocale();
  const endRef = useRef(null);
  const hasMessages = messages.length > 0;

  // Keep the newest message, the typing dots or the contact form in view.
  useEffect(() => {
    if (!hasMessages) return;
    const reduceMotion = window.matchMedia?.('(prefers-reduced-motion: reduce)').matches;
    endRef.current?.scrollIntoView({ behavior: reduceMotion ? 'auto' : 'smooth', block: 'end' });
  }, [messages, isLoading, requiresLeadCapture, hasMessages]);

  return (
    <div className="chat">
      <header className="chat-header">
        <div className="chat-header-inner">
          <div className="chat-brand">
            <img src={logo} alt="" className="chat-brand-mark" />
            <div className="chat-brand-text">
              <h1 className="chat-brand-name">{t.brand}</h1>
              <span className="chat-brand-sub">{t.tagline}</span>
            </div>
          </div>

          <div className="chat-header-actions">
            {hasMessages && (
              <button
                type="button"
                className="chat-action"
                onClick={clearConversation}
                disabled={isLoading}
                aria-label={t.newChat}
                title={t.newChat}
              >
                <PlusIcon />
                <span>{t.newChat}</span>
              </button>
            )}

            <div className="lang-switch" role="group" aria-label={t.language}>
              <button
                type="button"
                lang="bn"
                aria-pressed={locale === 'bn'}
                onClick={() => setLocale('bn')}
              >
                বাং
              </button>
              <button
                type="button"
                lang="en"
                aria-pressed={locale === 'en'}
                onClick={() => setLocale('en')}
              >
                EN
              </button>
            </div>
          </div>
        </div>
      </header>

      <main className="chat-main">
        <div className="chat-column">
          <MessageList />
          {isLoading && <TypingIndicator />}
          {requiresLeadCapture && <LeadCaptureForm />}
          <div ref={endRef} aria-hidden="true" />
        </div>
      </main>

      <MessageInput />
    </div>
  );
};

const ChatInterface = () => (
  <LocaleProvider>
    <ConversationProvider>
      <ChatInterfaceContent />
    </ConversationProvider>
  </LocaleProvider>
);

export default ChatInterface;
