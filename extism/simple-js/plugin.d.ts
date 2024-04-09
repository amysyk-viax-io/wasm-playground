declare module 'main' {
    export function kvStoreRead(): I32;
    export function kvStoreWrite(): I32;
    export function simpleHttpPost(): I32;
}

declare module 'extism:host' {
    interface user {
        kv_write(key: I64, value: I64): I64;
        kv_read(key: I64): I64;
    }
}