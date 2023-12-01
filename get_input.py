import requests
import os.path
from datetime import date
from os import getenv

def main():
    day = date.today().strftime("%-d")
    year = date.today().strftime("%Y")
    file_path = 'src/main/resources/{}.txt'.format(day)
    if not os.path.isfile(file_path):
        url = "https://adventofcode.com/{}/day/{}/input".format(year,day)
        # Most easily get this after logging in with Chrome
        cookie = {"session": getenv('SESSION_COOKIE')}
        res = requests.get(url, cookies=cookie)
        puzzle_input = res.text[:-1]
        if 'DOCTYPE HTML PUBLIC' in puzzle_input:
            exit('Unable to authorize with session cookie')
        else:
            print(puzzle_input)
        
        f = open(file_path, "w")
        f.write(puzzle_input)
        f.close()

if __name__ == '__main__':
    main()