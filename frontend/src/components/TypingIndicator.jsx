import './TypingIndicator.css';

const TypingIndicator = () => {
  return (
    <div className="typing-indicator">
      <div className="typing-dots">
        <span></span>
        <span></span>
        <span></span>
      </div>
      <span className="typing-text">Agent is typing...</span>
    </div>
  );
};

export default TypingIndicator;

