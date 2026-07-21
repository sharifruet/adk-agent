import ReactMarkdown from 'react-markdown';
import logo from '../assets/logo.png';
import './Message.css';

const Message = ({ message }) => {
  const isUser = message.type === 'user';
  const timestamp = new Date(message.timestamp).toLocaleTimeString([], {
    hour: '2-digit',
    minute: '2-digit',
  });

  return (
    <div className={`message ${isUser ? 'user' : 'agent'}`}>
      {!isUser && <img src={logo} alt="" className="message-logo message-logo-left" />}
      <div className="message-content">
        <div className="message-text">
          {isUser ? (
            message.content
          ) : (
            <ReactMarkdown
              components={{
                ul: ({ children }) => <ul className="message-list">{children}</ul>,
                ol: ({ children }) => <ol className="message-list message-list-ordered">{children}</ol>,
                li: ({ children }) => <li className="message-list-item">{children}</li>,
                strong: ({ children }) => <strong className="message-bold">{children}</strong>,
                p: ({ children }) => <p className="message-paragraph">{children}</p>,
              }}
            >
              {message.content}
            </ReactMarkdown>
          )}
        </div>
        <div className="message-timestamp">{timestamp}</div>
      </div>
      {!isUser && <img src={logo} alt="" className="message-logo message-logo-right" />}
    </div>
  );
};

export default Message;

