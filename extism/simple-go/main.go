//go:build std
// +build std

package main

import (
	"encoding/json"
	"github.com/extism/go-pdk"
)

type SimpleRequest struct {
	ID string `json:"id"`
}

//go:wasmimport extism:host/user kv_read
func kvRead(uint64) uint64

//go:wasmimport extism:host/user kv_write
func kvWrite(uint64, uint64) uint64

//export kvStoreRead
func kvStoreRead() int32 {
	key := pdk.InputString()
	mem := pdk.AllocateString(key)
	defer mem.Free()
	ptr := kvRead(mem.Offset())
	rmem := pdk.FindMemory(ptr)
	response := string(rmem.ReadBytes())
	pdk.OutputString(response)

	return 0
}

//export kvStoreWrite
func kvStoreWrite() int32 {
	key := pdk.InputString()
	value := `value ` + key
	keyMem := pdk.AllocateString(key)
	defer keyMem.Free()
	valueMem := pdk.AllocateString(value)
	defer valueMem.Free()
	ptr := kvWrite(keyMem.Offset(), valueMem.Offset())
	rmem := pdk.FindMemory(ptr)
	response := string(rmem.ReadBytes())
	pdk.OutputString(response)

	return 0
}

//export simpleHttpPost
func simpleHttpPost() int32 {
	input := pdk.Input()
	req := pdk.NewHTTPRequest(pdk.MethodPost, "http://localhost:8080/simple/some-processing")
	req.SetHeader("Content-Type", "application/json")
	body, err := json.Marshal(SimpleRequest{ID: string(input)})
	if err != nil {
		pdk.SetError(err)
		return 1
	}

	req.SetBody(body)
	res := req.Send()

	if res.Status() != 200 {
		pdk.SetErrorString(`Got non 200 response ` + string(res.Status()))
		return 1
	}

	pdk.OutputMemory(res.Memory())

	return 0
}

func main() {}
