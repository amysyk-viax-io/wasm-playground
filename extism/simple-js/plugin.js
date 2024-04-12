const { kv_write, kv_read } = Host.getFunctions()

function kvStoreRead() {
    const key = Host.inputString()

    const offset = kv_read(Memory.fromString(key).offset)
    const response = Memory.find(offset).readString()

    Host.outputString(response)
}

function kvStoreWrite() {
    const key = Host.inputString()
    const value = `value ${key}`

    const offset = kv_write(Memory.fromString(key).offset, Memory.fromString(value).offset)
    Memory.find(offset).readString()
}

function simpleHttpPost() {
    const input = Host.inputString()
    const request = {
        method: "POST",
        url: "http://localhost:8080/simple/some-processing",
        headers: {
            "Content-Type": "application/json"
        }
    }
    const requestBody = JSON.stringify({id: input})
    const response = Http.request(request, requestBody)
    if (response.status !== 200) throw new Error(`Got non 200 response ${response.status}`)

    Host.outputString(response.body)
}

module.exports = {kvStoreRead, kvStoreWrite, simpleHttpPost}