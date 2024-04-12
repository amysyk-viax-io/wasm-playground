# wasm playground

## Compatibility

Java: 21

## Preparation

### Extism runtime installation

https://github.com/extism/java-sdk?tab=readme-ov-file#install-the-extism-runtime-dependency

### k6 installation

https://grafana.com/docs/k6/latest/set-up/install-k6/

## Build and run

```bash
EXTISM_CACHE_CONFIG=./wasmtime-config.toml ./gradlew bootRun
```