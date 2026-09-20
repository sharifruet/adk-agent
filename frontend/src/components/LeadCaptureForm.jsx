import { useEffect, useState } from 'react';
import { useConversation } from '../context/ConversationContext';
import { useLocale } from '../context/LocaleContext';
import './LeadCaptureForm.css';

const SUCCESS_VISIBLE_MS = 4000;
const EMPTY = { name: '', phone: '', email: '' };

const CheckIcon = () => (
  <svg
    viewBox="0 0 20 20"
    width="20"
    height="20"
    aria-hidden="true"
    fill="none"
    stroke="currentColor"
    strokeWidth="2"
    strokeLinecap="round"
    strokeLinejoin="round"
  >
    <path d="m4.5 10.5 3.5 3.5 7.5-8" />
  </svg>
);

const LeadCaptureForm = () => {
  const { submitCustomerInfo, setRequiresLeadCapture } = useConversation();
  const { t } = useLocale();
  const [formData, setFormData] = useState(EMPTY);
  const [submitting, setSubmitting] = useState(false);
  const [submitted, setSubmitted] = useState(false);
  const [error, setError] = useState(null);

  // Once the details are in, show the confirmation briefly, then hand the
  // conversation back to the customer.
  useEffect(() => {
    if (!submitted) return undefined;
    const timer = setTimeout(() => setRequiresLeadCapture(false), SUCCESS_VISIBLE_MS);
    return () => clearTimeout(timer);
  }, [submitted, setRequiresLeadCapture]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    if (!formData.name.trim() || !formData.phone.trim() || !formData.email.trim()) {
      setError(t.lead.required);
      return;
    }

    setSubmitting(true);
    try {
      await submitCustomerInfo({
        name: formData.name.trim(),
        phone: formData.phone.trim(),
        email: formData.email.trim(),
      });
      setSubmitted(true);
    } catch {
      setError(t.lead.failed);
    } finally {
      setSubmitting(false);
    }
  };

  const handleCancel = () => {
    setRequiresLeadCapture(false);
  };

  if (submitted) {
    return (
      <div className="lead lead-success" role="status">
        <span className="lead-success-icon">
          <CheckIcon />
        </span>
        <div>
          <p className="lead-title">{t.lead.successTitle}</p>
          <p className="lead-body">{t.lead.successBody}</p>
        </div>
      </div>
    );
  }

  return (
    <form className="lead" onSubmit={handleSubmit} noValidate>
      <p className="lead-title">{t.lead.title}</p>
      <p className="lead-body">{t.lead.body}</p>

      <div className="lead-fields">
        <div className="lead-field">
          <label htmlFor="lead-name">{t.lead.name}</label>
          <input
            id="lead-name"
            name="name"
            type="text"
            autoComplete="name"
            value={formData.name}
            onChange={handleChange}
            placeholder={t.lead.namePlaceholder}
            disabled={submitting}
            required
          />
        </div>

        <div className="lead-field">
          <label htmlFor="lead-phone">{t.lead.phone}</label>
          <input
            id="lead-phone"
            name="phone"
            type="tel"
            inputMode="tel"
            autoComplete="tel"
            value={formData.phone}
            onChange={handleChange}
            placeholder={t.lead.phonePlaceholder}
            disabled={submitting}
            required
          />
        </div>

        <div className="lead-field">
          <label htmlFor="lead-email">{t.lead.email}</label>
          <input
            id="lead-email"
            name="email"
            type="email"
            inputMode="email"
            autoComplete="email"
            value={formData.email}
            onChange={handleChange}
            placeholder={t.lead.emailPlaceholder}
            disabled={submitting}
            required
          />
        </div>
      </div>

      {error && (
        <p className="lead-error" role="alert">
          {error}
        </p>
      )}

      <div className="lead-actions">
        <button type="submit" className="lead-submit" disabled={submitting}>
          {submitting ? t.lead.submitting : t.lead.submit}
        </button>
        <button type="button" className="lead-cancel" onClick={handleCancel} disabled={submitting}>
          {t.lead.cancel}
        </button>
      </div>
    </form>
  );
};

export default LeadCaptureForm;
