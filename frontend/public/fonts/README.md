# Fonts

The app renders in **Nikosh**, the Bangla Unicode font published by the
Bangladesh Computer Council (BCC). It is not on Google Fonts or any public CDN,
so it has to be self-hosted from this folder.

## Required files

Drop these two files in this directory (they are referenced by the `@font-face`
rule in `src/index.css`):

- `Nikosh.woff2` — used by every modern browser
- `Nikosh.ttf` — fallback for older browsers

## Where to get it

`Nikosh.ttf` is distributed by BCC and bundled with Avro Keyboard and Bijoy. On
a Windows machine that already has Avro installed, it is at
`C:\Windows\Fonts\Nikosh.ttf`.

## Converting to woff2

```bash
# pip install fonttools brotli
fonttools ttLib.woff2 compress -o Nikosh.woff2 Nikosh.ttf
```

Until these files are added, the app falls back to `Noto Sans Bengali` /
`SolaimanLipi` / the system Bangla font — the layout is unaffected, only the
typeface differs.
