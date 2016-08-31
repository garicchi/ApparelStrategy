from PIL import Image
import colorsys

class Pixel:

    def __init__(self,filepath):
        self.filepath = filepath

    def get_saturation(self):
        im = Image.open(self.filepath)
        # RGBに変換
        rgb_im = im.convert('RGB')
        # 画像サイズを取得
        size = rgb_im.size
        start_x = size[0]/3
        end_x = size[0] - start_x
        start_y = size[1]/3
        end_y = size[1] - start_y

        values = []
        # x
        for x in range(size[0]):
            # y
            for y in range(size[1]):
                if x>start_x and x<end_x and y>start_y and y<end_y:
                    # ピクセルを取得
                    r, g, b = rgb_im.getpixel((x, y))
                    (hue, sat, val) = colorsys.rgb_to_hsv(r/255,g/255,b/255)
                    if hue is not None:
                        values.append(sat)
        return (sum(values)/len(values))*100


if __name__ == '__main__':
    pixel = Pixel('s21.jpg')
    print(pixel.get_saturation())