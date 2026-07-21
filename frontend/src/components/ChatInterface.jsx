import { ConversationProvider, useConversation } from '../context/ConversationContext';
import MessageList from './MessageList';
import MessageInput from './MessageInput';
import LeadCaptureForm from './LeadCaptureForm';
import TypingIndicator from './TypingIndicator';
import logo from '../assets/logo.png';
import './ChatInterface.css';

const ChatInterfaceContent = () => {
  const { requiresLeadCapture, isLoading } = useConversation();

  return (
    <div className="chat-interface">
      <div className="chat-header">
        <img src={logo} alt="জীবন বীমা কর্পোরেশন" className="chat-header-logo" />
        <div className="chat-header-text">
          <h1>জীবন বীমা কর্পোরেশন</h1>
          <p>এআই সেবা এজেন্ট</p>
        </div>
      </div>
      
      <div className="chat-messages-container">
        <div className="chat-watermark" aria-hidden="true">
          <img src={logo} alt="" />
        </div>
        <div className="chat-messages-content">
          <MessageList />
          {isLoading && <TypingIndicator />}
        </div>
      </div>

      {requiresLeadCapture && (
        <LeadCaptureForm />
      )}

      <MessageInput />
    </div>
  );
};

const ChatInterface = () => {
  return (
    <ConversationProvider>
      <ChatInterfaceContent />
    </ConversationProvider>
  );
};

export default ChatInterface;

