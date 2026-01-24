import './Message.css';

const Message = ({ message }) => {
  const isUser = message.type === 'user';
  const timestamp = new Date(message.timestamp).toLocaleTimeString([], {
    hour: '2-digit',
    minute: '2-digit',
  });

  return (
    <div className={`message ${isUser ? 'user' : 'agent'}`}>
      <div className="message-content">
        <div className="message-text">{message.content}</div>
        <div className="message-timestamp">{timestamp}</div>
      </div>
    </div>
  );
};

export default Message;

