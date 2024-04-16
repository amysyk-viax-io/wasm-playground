```bash
cargo build --target wasm32-wasi --release
wasm-opt -O3 -o plugin.wasm target/wasm32-wasi/release/plugin.wasm
```

```bash
extism call plugin.wasm kvStoreRead --input "Benjamin"
```

```bash
extism call plugin.wasm simpleHttpPost --input "Benjamin" --allow-host "localhost"
```
