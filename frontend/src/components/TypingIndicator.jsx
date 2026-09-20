import { useLocale } from '../context/LocaleContext';
import logo from '../assets/logo.png';
import './TypingIndicator.css';

const TypingIndicator = () => {
  const { t } = useLocale();

  return (
    <div className="typing" role="status">
      <img src={logo} alt="" className="typing-avatar" />
      <div className="typing-dots" aria-hidden="true">
        <span />
        <span />
        <span />
      </div>
      <span className="sr-only">{t.typing}</span>
    </div>
  );
};

export default TypingIndicator;
