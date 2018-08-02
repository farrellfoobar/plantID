from urllib.request import urlopen
import urllib
import pickle
import os
from multiprocessing import Pool
import time
import random

TAXON_URL = 'http://ucjeps.berkeley.edu/eflora/eflora_display.php?tid='
START = '10025'
OUTPUT_FILE = 'E:\\jepsonCrawlerOutput'
MAX_TAXA_TO_CRAWL = 100000

ATTRIBUTES = {
    "Plant Group"       : ["Tree", "Grass", "Shrub", "Forb", "Succulent"],
    "Leaf Type"         : ["Simple", "Lobed", "Pinnate", "Compound/Deeply divided", "blade"],
    "Leaf Arrangement"  : ["Alternate", "Opposite", "Bundled", "Whorled", "basal", "Rosette"],
    "Growth Form"       : ["prostrate", "decumbent", "ascending", "erect", "mat", "clump-forming", "rosette", "basal", "vine"],
    "Flower Color"      : ["white", "yellow", "red", "orange", "blue", "purple", "green", "pink"],
    "Flower Symmetry"   : ["radial", "bilateral", "asymmetrical"]
}

# NOTE: If the key is found but not its synonym then the key is not registered
SYNONYMS = {
    "Tree": ["tree"],
    "Grass": ["grass"],
    "Shrub": ["shrub"],
    "Forb": ["forb"],
    "Succulent": ["succulent"],
    "Simple": ["simple"],
    "Lobed": ["lobed"],
    "Pinnate": ["pinnate"],
    "Compound/Deeply divided": ["compound"],
    "blade": ["blade"],
    "Alternate": ["alternate"],
    "Opposite": ["opposite"],
    "Bundled": ["bundled"],
    "Whorled": ["whorled"],
    "basal": ["basal"],
    "Rosette": ["rosette"],
    "prostrate": ["prostrate"],
    "decumbent": ["decumbent"],
    "ascending": ["ascending"],
    "erect": ["erect"],
    "mat": ["mat"],
    "clump-forming": ["clump-forming", "clump"],
    "rosette": ["rosette"],
    "basal": ["basal"],
    "vine": ["vine"],
    "white": ["white", "whitish"],
    "yellow": ["yellow", "yellowish"],
    "red": ["red", "redish"],
    "orange": ["orange", "orangish"],
    "blue": ["blue", "bluish"],
    "purple": ["purple", "purplish"],
    "green": ["green", "greenish"],
    "pink": ["pink", "pinkish"],
    "radial": ["radial"],
    "bilateral": ["bilateral"],
    "asymmetrical": ["asymmetrical"]
}

plant_nums = list()

def main():
    plant_nums = load_obj('plant_nums')
    taxa_info = load_obj('taxa_info')

    download_plant_images(plant_nums, taxa_info)

def download_plant_images(plant_nums, taxa_info):
    args = list()
    for x in range(len(plant_nums)):
        args.append([plant_nums[x], taxa_info[x][0], x/len(plant_nums)])

    if __name__ == '__main__':
        thread_pool = Pool(10)
        thread_pool.map(write_images_from_num, args)

def write_images_from_num(num_and_plant_name):
    num = num_and_plant_name[0]
    plant_name = num_and_plant_name[1]
    prog = num_and_plant_name[2]

    print('Proccessing plant#: ' + str(num))
    print('{0:.2%} done'.format(prog))

    try:
        html = get_html(num)
    except urllib.error.HTTPError as e:
        raise e

    img_string = r'http://calphotos.berkeley.edu/imgs/'
    key_len = len('0000_0000/0000/0000')

    try:
        os.makedirs(OUTPUT_FILE + os.sep + plant_name)
    except: pass

    cur = 0
    i = 0
    index = html.find(img_string, cur)
    while index != -1:
        start_key_index = index + len('http://calphotos.berkeley.edu/imgs/128x192/')
        key = html[start_key_index:start_key_index+key_len]
        img_to_get = 'https://calphotos.berkeley.edu/imgs/512x768/' + key + '.jpeg'

        try:
            img_name = OUTPUT_FILE + os.sep + plant_name + '\\' + str(i) + '.jpeg'
            if not os.path.isfile(img_name):
                urllib.request.urlretrieve(img_to_get, img_name)
        except Exception as e:
            print(img_to_get)
            print(e)
            print('')

        i+=1
        cur = index+1
        index = html.find(img_string, cur)

def iterate_taxa():
    taxa_info = list()
    taxon_number = START
    plant_nums = list()

    try:
        count = 0
        while count < MAX_TAXA_TO_CRAWL:
            html = get_html(taxon_number)
            plant_nums.append(taxon_number)
            taxa_info.append(get_attribs_from_html(html))
            taxon_number = get_next_taxon_number(html)
            count += 1
            if count % 100 == 0:
                print(100 * count / 9950)
                save_obj(taxa_info, "taxa_info")
                save_obj(plant_nums, "plant_nums")

    except Exception:
        save_obj(taxa_info, "taxa_info")
        save_obj(plant_nums, "plant_nums")

    return taxa_info


def get_attribs_from_html(html):
    # these effectively map out terminology to their terminology
    growth_form_section = get_text_between(html, '<b>' + 'Stem:' + '</b>', '<b>').lower()
    leaf_section = get_text_between(html, '<b>' + 'Leaf:' + '</b>', '<b>').lower()
    flower_section = get_text_between(html, 'Flower:' + '</b>', '<b>').lower()
    group_section = get_text_between(html, 'Habit:' + '</b>', '<b>').lower()

    out = list()
    out.append(get_taxon_name(html))
    out.append(get_taxon_common_name(html))

    for characteristic in ATTRIBUTES:
        if characteristic == "Plant Group":
            section = group_section
        elif characteristic == "Leaf Type" or characteristic == "Leaf Arrangement":
            section = leaf_section
        elif characteristic == "Growth Form":
            section = growth_form_section
        elif characteristic == "Flower Color" or characteristic == "Flower Symmetry":
            section = flower_section
        else:
            section = growth_form_section

        next_attrib = list()

        for value in ATTRIBUTES[characteristic]:
            for synonyms in SYNONYMS[value]:
                if synonyms in section:
                    next_attrib.append(value)

        out.append( next_attrib )

    return out


def save_obj(obj, name):
    with open(OUTPUT_FILE + os.sep + name + '.pkl', 'wb') as f:
        pickle.dump(obj, f, pickle.HIGHEST_PROTOCOL)


def load_obj(name):
    with open(OUTPUT_FILE + os.sep + name + '.pkl', 'rb') as f:
        return pickle.load(f)


def get_html(taxon_number):
    max_retries = 10
    try:
        response = urlopen(TAXON_URL + str(taxon_number))
    except urllib.error.HTTPError as e:
        if e.code == 403:
            tries = 1
            while tries < max_retries:
                print("403 Error while handeling url: " + TAXON_URL + str(taxon_number) + " Retrying in a few seconds.")
                time.sleep(random.randint(3, 15))
                response = urlopen(TAXON_URL + str(taxon_number))
                tries+=1

            if tries == max_retries:
                raise e

        elif e.code == 404:
            print("HTTP Error 404 on url: " + TAXON_URL + str(taxon_number))
        else:
            raise e

    html = str(response.read())

    html = html.replace(get_text_between(html, 'div id="familydesc"', '</blockquote></div>'), "")
    html = html.replace(get_text_between(html, '<div id="genusdesc" class="initiallyHidden" style="display: block;">', '</blockquote>'), "")

    return html


def get_taxon_name(html):
    return get_text_between(html, r'<span align =\'center\' class=\'pageLargeHeading\'>', '<')


def get_taxon_common_name(html):
    return get_text_between(html, r'<span align =\'center\' class=\'pageMajorHeading\'>', '<')


def get_next_taxon_number(html):
    end_of_next_taxon_url = html.find('"><IMG SRC="http://ucjeps.berkeley.edu/icons/right.gif" BORDER=2 ALT="Next taxon">')
    start_of_next_taxon_url = html.find('"eflora_display.php?tid=', end_of_next_taxon_url - len('"eflora_display.php?tid=XXXXXXXXXX'), end_of_next_taxon_url)
    taxon_number = html[len('"eflora_display.php?tid=') + start_of_next_taxon_url:end_of_next_taxon_url]

    if taxon_number == START:
        raise StopIteration("Wrapped taxon urls: stopping ")

    return taxon_number


def get_text_between(html, start_unique, end):
    """
    :param html: The HTML page, as a string.
    :param start_unique: The substring that pre-appends the string to be returned. This must be unique to :param html.
    :param end: The substring that appends the string to be returned. The first occurrence of this string signals the end
    of the string to be returned
    :return: The substring that occurs between the first occurrence of start_unique and the first occurrence
    thereafter of end
    """
    start_of_str = html.find(start_unique)

    if start_of_str == -1:
        return ""

    end_of_str = html.find(end, start_of_str + len(start_unique)+1)

    return html[start_of_str+len(start_unique):end_of_str]

main()
