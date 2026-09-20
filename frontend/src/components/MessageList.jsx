import { useConversation } from '../context/ConversationContext';
import { useLocale } from '../context/LocaleContext';
import Message from './Message';
import EmptyState from './EmptyState';
import './MessageList.css';

const MessageList = () => {
  const { messages, error, isLoading, sendMessage } = useConversation();
  const { t } = useLocale();

  if (messages.length === 0) {
    return <EmptyState onPick={sendMessage} disabled={isLoading} />;
  }

  const errorText = error ? t.errors[error.kind] || t.errors.generic : null;
  const showDetail = error && (error.kind === 'server' || error.kind === 'client') && error.detail;

  return (
    <div className="messages" role="log" aria-live="polite">
      {messages.map((message) => (
        <Message key={message.id} message={message} />
      ))}
      {error && (
        <div className="messages-error" role="alert">
          {errorText}
          {showDetail && <small>{error.detail}</small>}
        </div>
      )}
    </div>
  );
};

export default MessageList;
