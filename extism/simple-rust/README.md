```bash
cargo build --target wasm32-wasi --release
cp target/wasm32-wasi/release/plugin.wasm plugin.wasm
```

```bash
extism call plugin.wasm kvStoreRead --input "Benjamin"
```

```bash
extism call plugin.wasm simpleHttpPost --input "Benjamin" --allow-host "localhost"
```
