import { defineConfig, type Plugin } from 'vite';
import * as path from 'path';

// JavaFX WebView doesn't support <script type="module"> — convert to classic script
function javafxCompatPlugin(): Plugin {
  return {
    name: 'javafx-compat',
    enforce: 'post',
    transformIndexHtml(html) {
      return html
        .replace(/ type="module"/g, '')
        .replace(/ crossorigin/g, '')
        .replace('<script src=', '<script defer src=');
    },
  };
}

export default defineConfig({
  root: __dirname,
  server: {
    port: 5173,
    host: true,
    fs: {
      allow: [
        path.resolve(__dirname, '..', '..', '..'),
      ],
    },
  },
  base: './',
  plugins: [javafxCompatPlugin()],
  build: {
    outDir: 'dist',
    target: 'es2020',
    rollupOptions: {
      output: {
        format: 'iife',
        entryFileNames: 'assets/[name]-[hash].js',
      },
    },
  },
});
