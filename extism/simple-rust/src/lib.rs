use extism_pdk::*;

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

#[plugin_fn]
pub fn simpleHttpPost(input: String) -> FnResult<Vec<u8>> {
    let request = HttpRequest::new("http://localhost:8080/simple/some-processing")
        .with_method("POST")
        .with_header("Content-Type", "application/json");
    let res = http::request::<String>(&request, Some(format!("{{\"id\": \"{input}\"}}").to_string()))?;

    Ok(res.body())
}