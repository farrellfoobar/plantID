from urllib.request import urlopen
import logging

TAXON_URL = 'http://ucjeps.berkeley.edu/eflora/eflora_display.php?tid='
START = '10025'
MAX_TAXA_TO_CRAWL = 30

ATTRIBUTES = {
    "Plant Group"       : ["Tree", "Grass", "Shrub", "Forb", "Succulent"],
    "Leaf Type"         : ["Simple", "Lobed", "Pinnate", "Compound/Deeply divided", "blade"],
    "Leaf Arrangement"  : ["Alternate", "Opposite", "Bundled", "Whorled", "basal", "Rosette"],
    "Growth Form"       : ["prostrate", "decumbent", "ascending", "erect", "mat", "clump-forming", "rosette", "basal", "vine"],
    "Flower Color"      : ["white", "yellow", "red", "orange", "blue", "purple", "green", "pink"],
    "Flower Symmetry"   : ["radial", "bilateral", "asymmetrical"]
}

SYNONYMS = {
    "Tree": ["tree"],
    "Grass": ["grass"],
    "Shrub": ["shrub"],
    "Forb": ["forb"],
    "Succulent": ["succulent"],
    "Simple": ["simple"],
    "Lobed": ["lobed"],
    "Pinnate": ["pinnate", "pinnately"],
    "Compound/Deeply divided": ["compound", "divided"],
    "blade": ["blade"],
    "Alternate": ["alternate", "alternately"],
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
    "clump-forming": ["clump-forming", "clump", "clump"],
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
    "asymmetrical": ["asymmetrical", "asymmetrically"]
}

def main():
    alist = iterate_taxa()

    for x in alist:
        print(x)

def iterate_taxa():
    taxa_info = list()
    taxon_number = START

    try:
        count = 0
        while count < MAX_TAXA_TO_CRAWL:
            html = get_html(taxon_number)

            taxa_info.append(get_attribs_from_html(html))

            taxon_number = get_next_taxon_number(html)
            count += 1
    except StopIteration:
        pass  # ignore

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

        out.append(str(next_attrib)) # we cast to str to look like json

    return out


def get_html(taxon_number):
    logging.info("trying to open: " + TAXON_URL + taxon_number)
    response = urlopen(TAXON_URL + taxon_number)
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
