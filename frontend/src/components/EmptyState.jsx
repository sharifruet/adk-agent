import { useLocale } from '../context/LocaleContext';
import logo from '../assets/logo.png';
import './EmptyState.css';

const EmptyState = ({ onPick, disabled }) => {
  const { t } = useLocale();

  return (
    <section className="empty" aria-labelledby="empty-title">
      <img src={logo} alt="" className="empty-mark" />
      <h2 id="empty-title" className="empty-title">
        {t.emptyTitle}
      </h2>
      <p className="empty-body">{t.emptyBody}</p>

      <ul className="empty-suggestions">
        {t.suggestions.map((suggestion) => (
          <li key={suggestion}>
            <button type="button" onClick={() => onPick(suggestion)} disabled={disabled}>
              {suggestion}
            </button>
          </li>
        ))}
      </ul>
    </section>
  );
};

export default EmptyState;
