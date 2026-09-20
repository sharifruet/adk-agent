/**
 * Every static string in the interface, in Bangla and English.
 * The agent itself answers in whichever language the customer writes in;
 * this only covers the chrome around the conversation.
 */
export const strings = {
  bn: {
    brand: 'জীবন বীমা কর্পোরেশন',
    tagline: 'এআই সেবা সহকারী',
    newChat: 'নতুন আলাপ',
    language: 'ভাষা',
    you: 'আপনি',
    assistant: 'সহকারী',
    typing: 'উত্তর লেখা হচ্ছে',

    emptyTitle: 'জীবন বীমা নিয়ে যেকোনো প্রশ্ন করুন',
    emptyBody:
      'পলিসি, প্রিমিয়াম, মেয়াদ ও সুবিধা সম্পর্কে জেনে নিন। বাংলা বা ইংরেজি, যে ভাষায় লিখবেন সেই ভাষাতেই উত্তর পাবেন।',
    suggestions: [
      'কোন পলিসি আমার জন্য উপযুক্ত হবে?',
      'মাসিক সঞ্চয় স্কিম সম্পর্কে জানতে চাই',
      'পেনশন বীমায় মাসে কত প্রিমিয়াম দিতে হয়?',
      'সন্তানের ভবিষ্যতের জন্য কোন বীমা ভালো?',
    ],

    placeholder: 'আপনার প্রশ্ন লিখুন…',
    placeholderLocked: 'আগে উপরের ফর্মটি পূরণ করুন',
    send: 'পাঠান',
    disclaimer:
      'এআই সহকারী ভুল করতে পারে। পলিসি নেওয়ার আগে জীবন বীমা কর্পোরেশনের অফিসে যাচাই করে নিন।',

    errors: {
      network: 'সংযোগ পাওয়া যায়নি। ইন্টারনেট সংযোগ দেখে আবার চেষ্টা করুন।',
      server: 'সার্ভারে সমস্যা হয়েছে। একটু পরে আবার চেষ্টা করুন।',
      generic: 'বার্তা পাঠানো যায়নি। আবার চেষ্টা করুন।',
    },

    lead: {
      title: 'যোগাযোগের তথ্য',
      body: 'আমাদের একজন এজেন্ট আপনার সাথে যোগাযোগ করবেন। এজন্য আপনার তথ্য দিন।',
      name: 'পুরো নাম',
      namePlaceholder: 'যেমন: রহিমা খাতুন',
      phone: 'ফোন নম্বর',
      phonePlaceholder: '01XXXXXXXXX',
      email: 'ইমেইল',
      emailPlaceholder: 'name@example.com',
      submit: 'তথ্য পাঠান',
      submitting: 'পাঠানো হচ্ছে…',
      cancel: 'এখন নয়',
      required: 'তিনটি ঘরই পূরণ করুন।',
      failed: 'তথ্য পাঠানো যায়নি। আবার চেষ্টা করুন।',
      successTitle: 'ধন্যবাদ, আপনার তথ্য পেয়েছি',
      successBody: 'একজন এজেন্ট সাধারণত ২৪ ঘণ্টার মধ্যে আপনার সাথে যোগাযোগ করবেন।',
    },
  },

  en: {
    brand: 'Jiban Bima Corporation',
    tagline: 'AI service assistant',
    newChat: 'New chat',
    language: 'Language',
    you: 'You',
    assistant: 'Assistant',
    typing: 'Writing a reply',

    emptyTitle: 'Ask anything about life insurance',
    emptyBody:
      'Learn about policies, premiums, terms and benefits. Write in Bangla or English and the answer comes back in the same language.',
    suggestions: [
      'Which policy would suit me?',
      'Tell me about the monthly savings scheme',
      'How much is the monthly premium for pension insurance?',
      'Which plan is best for my child’s future?',
    ],

    placeholder: 'Write your question…',
    placeholderLocked: 'Complete the form above first',
    send: 'Send',
    disclaimer:
      'The AI assistant can make mistakes. Confirm details with a Jiban Bima Corporation office before buying a policy.',

    errors: {
      network: 'No connection. Check your internet and try again.',
      server: 'Something went wrong on the server. Try again in a moment.',
      generic: 'The message could not be sent. Try again.',
    },

    lead: {
      title: 'Contact details',
      body: 'One of our agents will get in touch with you. Share your details so we can connect you.',
      name: 'Full name',
      namePlaceholder: 'e.g. Rahima Khatun',
      phone: 'Phone number',
      phonePlaceholder: '01XXXXXXXXX',
      email: 'Email',
      emailPlaceholder: 'name@example.com',
      submit: 'Send details',
      submitting: 'Sending…',
      cancel: 'Not now',
      required: 'Fill in all three fields.',
      failed: 'Your details could not be sent. Try again.',
      successTitle: 'Thanks, we have your details',
      successBody: 'An agent will usually contact you within 24 hours.',
    },
  },
};

export const LOCALES = Object.keys(strings);
