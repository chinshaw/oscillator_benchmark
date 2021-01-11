import requests
import time
import matplotlib as mpl
from concurrent.futures import ThreadPoolExecutor
import matplotlib.pyplot as plt

def get_url(url):
    start = time.time()
    res = requests.get(url)
    return time.time() - start


def make_requests(port, iterations, samples):
    list_urls = [f"http://localhost:{port}/oscillators/{samples}".format(port, samples)] * iterations


    with ThreadPoolExecutor(max_workers=10) as pool:
        start = time.time()
        times = list(pool.map(get_url, list_urls))
        inner_time = sum(times)
        outer_time = time.time() - start

    return outer_time, inner_time


if __name__ == '__main__':
    total_samples = 100_000
    iterations = 1000


    all_times = list()

    print("Running Kotlin sample")
    total_time, inner_time = make_requests(8085, iterations, total_samples)
    all_times.append(total_time)
    print("Kotlin Took {}, {}".format(total_time, inner_time))

    print("Running NodeJS Sample")
    total_time, inner_time = make_requests(8025, iterations, total_samples)
    all_times.append(total_time)
    print("NodeJS Took {}, {}".format(total_time, inner_time))

    print("Running Rust Sample")
    total_time, inner_time = make_requests(8000, iterations, total_samples)
    all_times.append(total_time)
    print("Rust Times {}, {}".format(total_time, inner_time))

    langs = ['kotlin', 'nodejs', 'rust']

    plt.bar(langs, all_times, align='center')
    plt.xticks(langs, langs)
    plt.show()
