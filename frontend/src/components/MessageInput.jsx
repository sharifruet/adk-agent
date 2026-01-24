import { useState } from 'react';
import { useConversation } from '../context/ConversationContext';
import './MessageInput.css';

const MessageInput = () => {
  const [input, setInput] = useState('');
  const { sendMessage, isLoading, requiresLeadCapture } = useConversation();

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!input.trim() || isLoading) return;

    const question = input.trim();
    setInput('');
    await sendMessage(question);
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSubmit(e);
    }
  };

  return (
    <form className="message-input" onSubmit={handleSubmit}>
      <input
        type="text"
        value={input}
        onChange={(e) => setInput(e.target.value)}
        onKeyPress={handleKeyPress}
        placeholder={requiresLeadCapture ? "Please fill out the form above first" : "Type your message..."}
        disabled={isLoading || requiresLeadCapture}
        className="message-input-field"
      />
      <button
        type="submit"
        disabled={!input.trim() || isLoading || requiresLeadCapture}
        className="message-send-button"
      >
        Send
      </button>
    </form>
  );
};

export default MessageInput;

