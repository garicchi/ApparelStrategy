import tweepy
import requests

class PostTwitter:

    def __init__(self,consumer_key,consumer_secret,access_token,access_token_secret):
        self.access_token = access_token
        self.access_token_secret = access_token_secret
        self.consumer_key = consumer_key
        self.consumer_secret = consumer_secret
        self.auth = tweepy.OAuthHandler(self.consumer_key, self.consumer_secret)
        self.auth.set_access_token(self.access_token, self.access_token_secret)
        self.api = tweepy.API(self.auth)

    def post_image_url(self,status,url):
        res = requests.get(url, stream=True)
        filepath = 'temp.jpg'
        with open(filepath, "wb") as fp:
            fp.write(res.raw.data)

        self.api.update_with_media(filepath, status)