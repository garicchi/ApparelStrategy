# -*- coding: utf-8 -*-
import json
from watson_developer_cloud import NaturalLanguageClassifierV1

def season_classification(test01):
    credentials = ''

    with open('credentials.json', 'r') as f:
                data = json.load(f)
                credentials = data['credentials']

    natural_language_classifier = NaturalLanguageClassifierV1(username=credentials['username'],
                                                              password=credentials['password'])

    """
    with open('nlc_menu_train.csv', 'rb') as training_data:
        classifier = natural_language_classifier.create(training_data=training_data,
                                                        name='My Classfier',
                                                        language='ja')
        #APIレスポンスの表示
        print(json.dumps(classifier, indent=2))
    """

    data_json = natural_language_classifier.list()
    classifier = data_json['classifiers'][0]
    classifier_id = classifier['classifier_id']

    status = natural_language_classifier.status(classifier_id)
    print(json.dumps(status, indent=2))

    results = natural_language_classifier.classify(classifier_id, test01)
    #print(json.dumps(results, indent=2))

    top_class = results['top_class']
    if top_class == 'spring':
        key1 = '春'
    elif top_class == 'summer':
        key1 = '夏'
    elif top_class == 'autumn':
        key1 = '秋'
    elif top_class == 'winter':
        key1 = '冬'
    else :
        key1 = 'null'

    return key1

if __name__ == '__main__':
    print(season_classification("冬"))