```bash
go mod init viax.io/simple-go
```

```bash
go get github.com/extism/go-pdk
```

```bash
tinygo build -o plugin.wasm -target wasi main.go
```

```bash
extism call plugin.wasm simpleHttpPost --input "Benjamin" --wasi --allow-host "localhost"
```