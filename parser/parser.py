"""
parser.py

Loads and parses the raw AMT data, assembles a parallel corpus consisting of an English
Language file weakly aligned with a separate Machine Language file.
"""
__author__ = 'Sidd Karamcheti'

import os

raw_data_dir = "../data/raw"
english_corpus = "../data/corpus/english.txt"
machine_corpus = "../data/corpus/machine.txt"


def parse():
    """
    Iterate through all data files, load English/Machine commands, write to two
    corpus files.
    """
    english, machine = [], []
    for directory in os.listdir(raw_data_dir):
        if not directory == '.DS_Store' and "Turk" in directory:
            dir_path = os.path.join(raw_data_dir, directory)
            for example in os.listdir(dir_path):
                if not example == '.DS_Store':
                    with open(os.path.join(dir_path, example), 'r') as f:
                        lines = f.readlines()
                        assert len(lines) == 2
                        m, e = lines[0], lines[1]
                        machine.append(m)
                        english.append(e)

    assert len(english) == len(machine)
    with open(english_corpus, 'w') as f:
        f.write("".join(english))
    with open(machine_corpus, 'w') as f:
        f.write("".join(machine))


if __name__ == "__main__":
    # Parse
    parse()