const express = require('express')
const app = express()
const port = 8025

const generateRandom = (size) => {
    const vec = [];
    while(vec.length <= size) {
        let r = Math.floor(Math.random() * 100) + 1;
        vec.push(r);
    }
    return vec;
}

const stddev = (vec) => {
    const sum = vec.reduce((a, b) => a + b, 0);
    const mean = sum / vec.length;
    let variance = vec.map(v => {let delta = v - mean; return delta * delta})
        .reduce((a,b) => a + b, 0) / vec.length;
    return Math.sqrt(variance);
}

app.get('/oscillators/:samples', (req, res) => {
    let samples = parseInt(req.params['samples'])
    let vec = generateRandom(samples)
    res.send({'v': vec, 'stddev': stddev(vec)})
})

app.listen(port, () => {
    console.log(`Example app listening at http://localhost:${port}`)
})
