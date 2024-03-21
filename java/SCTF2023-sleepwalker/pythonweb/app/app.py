import requests
from flask import Flask, render_template, request, redirect, session, render_template_string
SleepWalker = Flask(__name__)
SleepWalker.config['SECRET_KEY'] = "xxx"
@SleepWalker.route('/')
def index():
    session['CTFER'] = "假如我说这个session一点用都没有你会信我吗?"
    return render_template('index.html')
@SleepWalker.route("/breakme")
def breakme():
    answer = request.args.get('cmd')    
    if answer is not None:
        blacklist = ['0"', '.','"','system', 'eval', 'exec', 'popen', 'subprocess',
                    'posix', 'builtins', 'namespace', 'read', 'self', 'mro', 'base',
                    'global', 'init', 'chr', 'value', 'pop', 'import',
                    'include','request', '{{', '}}','config','=','lipsum','~','url_for']
        for i in blacklist:
            if i in answer:
                answer = i
                return answer
                break
    return render_template_string(answer)
@SleepWalker.route("/heartbeat")
def status():
    server_url="http://server/client/status"
    requests.get(url=server_url)
    return "Seriously,I dont know whether is down or up XD!"
@SleepWalker.route("/server/status")
def serverstatus():
    server_url = f"http://server/server/status?{request.args.get('args')}="+request.args.get("hello")
    r=requests.get(url=server_url)
    content=r.text
    if r.status_code==200:
        return content
    return "something wrong with server"
if __name__ == '__main__':
    SleepWalker.run('0.0.0.0', 5000, debug=False)
