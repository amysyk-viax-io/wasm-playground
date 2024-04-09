```bash
extism-js plugin.js -i plugin.d.ts -o plugin.wasm
```

```bash
extism call plugin.wasm greet --input="Benjamin" --wasi --log-level=info
```