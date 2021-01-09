#![feature(proc_macro_hygiene, decl_macro)]


#[macro_use] extern crate rocket;

use serde::Serialize;
use rand::Rng;
use rocket_contrib::json::Json;

#[derive(Serialize)]
struct OscillatorResponse {
    v: Vec<u8>,
    stddev: f64
}

#[get("/oscillators/<samples>")]
fn gen_oscillators(samples: i32) -> Json<OscillatorResponse> {
    let vec: Vec<u8> = generate_vec(samples as usize);
    let std_dev = stddev(&vec);

    Json(OscillatorResponse {
       v: vec,
       stddev: std_dev
    })
}

fn stddev(vec: &Vec<u8>) -> f64 {
    let sum: f64 = vec.iter().fold(0f64, |a,b| a + (*b as f64));
    let mean = sum / vec.len() as f64;
    let variance = vec.iter()
        .map(|x| { let delta = (*x as f64) - mean; delta * delta})
        .fold(0f64, |a,b| a + b);
    return variance.sqrt()
}

fn generate_vec(len: usize)->Vec<u8>  {
    let mut rng = rand::thread_rng();
    let mut vec = Vec::with_capacity(len);
    for _ in 0..len {
        vec.push(rng.gen::<u8>());
    }
    return vec;
}

fn main() {
    rocket::ignite().mount("/", routes![gen_oscillators]).launch();
}

