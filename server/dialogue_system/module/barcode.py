import subprocess


def read_qr(img_path):
    command = 'zbarimg -q ' + img_path
    p = subprocess.Popen(command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    out, err = p.communicate()
    dout = out.decode('utf-8')
    derr = err.decode('utf-8')
    result = dout.replace('QR-Code:', '').replace('\n','')
    return result