const formatters = {
  bn: new Intl.DateTimeFormat('bn-BD', { hour: 'numeric', minute: '2-digit' }),
  en: new Intl.DateTimeFormat('en', { hour: 'numeric', minute: '2-digit' }),
};

/**
 * Clock time for a message, in the interface language.
 * In Bangla this renders Bangla numerals (১২:৪১), in English Latin ones.
 */
export const formatTime = (value, locale) => {
  const date = value instanceof Date ? value : new Date(value);
  if (Number.isNaN(date.getTime())) return '';
  return (formatters[locale] || formatters.en).format(date);
};
