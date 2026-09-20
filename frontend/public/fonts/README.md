# Fonts

The interface uses two web fonts from Google Fonts, one per script:

- **Manrope** for Latin text (variable, 400–700)
- **Hind Siliguri** for Bangla text (400, 500, 600, 700)

Both are loaded from `index.html`. Manrope is listed first in the font stack
and has no Bengali glyphs, so the browser resolves Latin letters, digits and
punctuation to Manrope and every Bangla character to Hind Siliguri, character
by character. The stack lives in `src/index.css`.

## Nikosh (offline fallback)

**Nikosh**, the Bangla Unicode face published by the Bangladesh Computer
Council, is self-hosted here as a fallback for Bangla when Google Fonts cannot
be reached. It is confined to the Bengali block with `unicode-range`, so its
weaker Latin glyphs never appear.

- `Nikosh.woff2` — used by every modern browser
- `Nikosh.ttf` — fallback for older browsers

`Nikosh.ttf` is distributed by BCC and bundled with Avro Keyboard and Bijoy.
To regenerate the woff2:

```bash
# pip install fonttools brotli
fonttools ttLib.woff2 compress -o Nikosh.woff2 Nikosh.ttf
```
