import requests
import time
from concurrent.futures import ThreadPoolExecutor


def get_url(url):
    start = time.time()
    requests.get(url)
    return time.time() - start


def make_requests(port, iterations, samples):
    list_urls = [f"http://localhost:{port}/oscillators/{samples}".format(port, samples)] * iterations

    start = time.time()
    with ThreadPoolExecutor(max_workers=10) as pool:
        times = list(pool.map(get_url, list_urls))
        total_time = sum(times)

    outertime = time.time() - start
    return total_time, outertime


if __name__ == '__main__':
    total_samples = 100_000
    iterations = 1000

    print("Running Kotlin sample")
    total_time, outer_time = make_requests(8085, iterations, total_samples)
    print("Kotlin Took {}, {}".format(total_time, outer_time))

    print("Running NodeJS Sample")
    total_time, outer_time = make_requests(8025, iterations, total_samples)
    print("NodeJS Took {}, {}".format(total_time, outer_time))

    print("Running Rust Sample")
    total_time, outer_time = make_requests(8000, iterations, total_samples)
    print("Rust Times {}, {}".format(total_time, outer_time))
