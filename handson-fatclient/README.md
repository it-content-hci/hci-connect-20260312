# HCI Connect — Hands-on Fat Client (JavaFX)

Demonstrates embedding `@documedis/web-components` (`ProductDetails`) inside a **JavaFX fat client** via `WebView`.

## Prerequisites

- JDK 21 (auto-downloaded via Gradle toolchain if absent)
- Node.js + pnpm (for the webview UI)

## Quick start

```bash
# 1 — Build the webview bundle
cd webview
pnpm install
pnpm build
cd ..

# 2 — Run the JavaFX app
./gradlew run
```

## Architecture

```
App.java              JavaFX shell — toolbar + WebView window
WebviewServer.java    Embedded HTTP server serving webview/dist/
webview/src/main.ts   Boots ProductDetails with a demo access token
webview/src/app.ts    bootProductDetail() — mounts the web component
```

## Key integration points

- `window.setProductNumber(n)` — called from Java toolbar to swap the displayed product
- Access token is hardcoded to the demo environment (`DEMO_TOKEN_V1_B5_HCI_TZ6FFKOLVB`)
- JavaFX 21 / WebKit — modern enough to render the component CSS without polyfills
