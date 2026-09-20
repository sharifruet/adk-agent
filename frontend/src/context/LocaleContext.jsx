import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react';
import { strings } from '../i18n/strings';

const STORAGE_KEY = 'jbc-locale';
const DEFAULT_LOCALE = 'bn';

const LocaleContext = createContext(null);

const readStoredLocale = () => {
  try {
    const saved = localStorage.getItem(STORAGE_KEY);
    return saved in strings ? saved : DEFAULT_LOCALE;
  } catch {
    return DEFAULT_LOCALE;
  }
};

export const LocaleProvider = ({ children }) => {
  const [locale, setLocaleState] = useState(readStoredLocale);

  useEffect(() => {
    document.documentElement.lang = locale;
    try {
      localStorage.setItem(STORAGE_KEY, locale);
    } catch {
      /* private mode or blocked storage: the choice just won't persist */
    }
  }, [locale]);

  const setLocale = useCallback((next) => {
    if (next in strings) setLocaleState(next);
  }, []);

  const value = useMemo(
    () => ({ locale, setLocale, t: strings[locale] }),
    [locale, setLocale],
  );

  return <LocaleContext.Provider value={value}>{children}</LocaleContext.Provider>;
};

export const useLocale = () => {
  const context = useContext(LocaleContext);
  if (!context) {
    throw new Error('useLocale must be used within LocaleProvider');
  }
  return context;
};
