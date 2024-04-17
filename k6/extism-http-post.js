import {check} from 'k6';
import http from 'k6/http';
import {uuidv4} from 'https://jslib.k6.io/k6-utils/1.4.0/index.js';

export const options = {
    discardResponseBodies: false,
    scenarios: {
        wasm: {
            executor: 'shared-iterations',
            vus: 50,
            iterations: 1000,
            maxDuration: '300s',
        },
    },
};

export default () => {
    const id = uuidv4()
    const headers = {
        'Content-Type': 'application/json'
    }
    const res = http.post('http://localhost:8080/extism/http-post', JSON.stringify({id}), {headers})

    check(res, {
        'is status 200': (r) => r.status === 200,
        'body is ok': (r) => r.body === `processing for ${id}`,
    })

    if (res.body !== `processing for ${id}`) {
        console.log(res.body)
    }
};