#![no_main]

use extism_pdk::*;
use serde::{Serialize};
use serde_json;

#[host_fn("extism:host/user")]
extern "ExtismHost" {
    fn kv_read(key: String) -> String;
    fn kv_write(key: String, value: String) -> String;
}

#[plugin_fn]
pub fn kvStoreRead(key: String) -> FnResult<String> {
    let output = unsafe { kv_read(key)? };
    Ok(output)
}

#[plugin_fn]
pub fn kvStoreWrite(key: String) -> FnResult<String> {
    let value = format!("value {key}");
    let output = unsafe { kv_write(key, value)? };
    Ok(output)
}

#[derive(Serialize)]
struct SimpleRequest {
    id: String
}

#[plugin_fn]
pub fn simpleHttpPost(input: String) -> FnResult<Vec<u8>> {
    let request = HttpRequest::new("http://localhost:8080/simple/some-processing")
        .with_method("POST")
        .with_header("Content-Type", "application/json");
    let payload = SimpleRequest { id: input };
    let res = http::request::<String>(&request, Some(serde_json::to_string(&payload).expect("Serialization failed")))?;

    Ok(res.body())
}