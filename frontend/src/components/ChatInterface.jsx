import { ConversationProvider, useConversation } from '../context/ConversationContext';
import MessageList from './MessageList';
import MessageInput from './MessageInput';
import LeadCaptureForm from './LeadCaptureForm';
import TypingIndicator from './TypingIndicator';
import './ChatInterface.css';

const ChatInterfaceContent = () => {
  const { requiresLeadCapture, isLoading } = useConversation();

  return (
    <div className="chat-interface">
      <div className="chat-header">
        <h1>Life Insurance Sales Agent</h1>
        <p>Ask me anything about life insurance</p>
      </div>
      
      <div className="chat-messages-container">
        <MessageList />
        {isLoading && <TypingIndicator />}
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

