import { useEffect, useRef } from 'react';
import { useConversation } from '../context/ConversationContext';
import Message from './Message';
import './MessageList.css';

const MessageList = () => {
  const { messages, error } = useConversation();
  const messagesEndRef = useRef(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  if (messages.length === 0) {
    return (
      <div className="message-list empty">
        <div className="empty-state">
          <p>Welcome! I'm here to help you understand your life insurance options.</p>
          <p>Ask me anything about life insurance, or tell me about your situation.</p>
        </div>
        <div ref={messagesEndRef} />
      </div>
    );
  }

  return (
    <div className="message-list">
      {messages.map((message) => (
        <Message key={message.id} message={message} />
      ))}
      {error && (
        <div className="error-message">
          <p>⚠️ {error}</p>
        </div>
      )}
      <div ref={messagesEndRef} />
    </div>
  );
};

export default MessageList;

