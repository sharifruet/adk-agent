import ReactMarkdown from 'react-markdown';
import remarkGfm from 'remark-gfm';
import { useLocale } from '../context/LocaleContext';
import { formatTime } from '../utils/formatTime';
import logo from '../assets/logo.png';
import './Message.css';

const remarkPlugins = [remarkGfm];

const markdownComponents = {
  // eslint-disable-next-line no-unused-vars
  a: ({ node, ...props }) => <a {...props} target="_blank" rel="noopener noreferrer" />,
};

const Message = ({ message }) => {
  const isUser = message.type === 'user';
  const { locale, t } = useLocale();
  const date = new Date(message.timestamp);

  return (
    <article className={`msg ${isUser ? 'msg-user' : 'msg-agent'}`}>
      {!isUser && <img src={logo} alt="" className="msg-avatar" />}
      <div className="msg-body">
        <span className="sr-only">{isUser ? t.you : t.assistant}: </span>
        <div className="msg-text">
          {isUser ? (
            message.content
          ) : (
            <ReactMarkdown remarkPlugins={remarkPlugins} components={markdownComponents}>
              {message.content}
            </ReactMarkdown>
          )}
        </div>
        <time className="msg-time" dateTime={date.toISOString()}>
          {formatTime(date, locale)}
        </time>
      </div>
    </article>
  );
};

export default Message;
