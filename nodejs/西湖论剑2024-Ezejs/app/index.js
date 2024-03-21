var express = require('express');
const fs = require('fs');
var _= require('lodash');
var bodyParser = require("body-parser");
const cookieParser = require('cookie-parser');
var ejs = require('ejs');
var path = require('path');
const putil_merge = require("putil-merge")
const fileUpload = require('express-fileupload');
const { v4: uuidv4 } = require('uuid');
const {value} = require("lodash/seq");
var app = express();
// 将文件信息存储到全局字典中
global.fileDictionary = global.fileDictionary || {};

app.use(fileUpload());
// 使用 body-parser 处理 POST 请求的数据
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
// 设置模板的位置
app.set('views', path.join(__dirname, 'views'));
// 设置模板引擎
app.set('view engine', 'ejs');
// 静态文件（CSS）目录
app.use(express.static(path.join(__dirname, 'public')))

app.get('/', (req, res) => {
    res.render('index');
});

app.get('/index', (req, res) => {

    res.render('index');
});
app.get('/upload', (req, res) => {
    //显示上传页面
    res.render('upload');
});

app.post('/upload', (req, res) => {
    const file = req.files.file;
    const uniqueFileName = uuidv4();
    const destinationPath = path.join(__dirname, 'uploads', file.name);
    // 将文件写入 uploads 目录
    fs.writeFileSync(destinationPath, file.data);
    global.fileDictionary[uniqueFileName] = file.name;
    res.send(uniqueFileName);
});


app.get('/list', (req, res) => {
    // const keys = Object.keys(global.fileDictionary);
    res.send(global.fileDictionary);
});
app.get('/file', (req, res) => {
    if(req.query.uniqueFileName){
        uniqueFileName = req.query.uniqueFileName
        filName = global.fileDictionary[uniqueFileName]

        if(filName){
            try{
                res.send(fs.readFileSync(__dirname+"/uploads/"+filName).toString())
            }catch (error){
                res.send("文件不存在！");
            }

        }else{
            res.send("文件不存在！");
        }
    }else{
        res.render('file')
    }
});


app.get('/rename',(req,res)=>{
    res.render("rename")
});
app.post('/rename', (req, res) => {
    if (req.body.oldFileName && req.body.newFileName && req.body.uuid){
        oldFileName = req.body.oldFileName
        newFileName = req.body.newFileName
        uuid = req.body.uuid
        if (waf(oldFileName)  && waf(newFileName) &&  waf(uuid)){
            uniqueFileName = findKeyByValue(global.fileDictionary,oldFileName)
            console.log(typeof uuid);
            if (uniqueFileName == uuid){
                putil_merge(global.fileDictionary,{[uuid]:newFileName},{deep:true})
                if(newFileName.includes('..')){
                    res.send('文件重命名失败！！！');
                }else{
                    fs.rename(__dirname+"/uploads/"+oldFileName, __dirname+"/uploads/"+newFileName, (err) => {
                        if (err) {
                            res.send('文件重命名失败！');
                        } else {
                            res.send('文件重命名成功！');
                        }
                    });
                }
            }else{
                res.send('文件重命名失败！');
            }

        }else{
            res.send('哒咩哒咩！');
        }

    }else{
        res.send('文件重命名失败！');
    }
});
function findKeyByValue(obj, targetValue) {
    for (const key in obj) {
        if (obj.hasOwnProperty(key) && obj[key] === targetValue) {
            return key;
        }
    }
    return null; // 如果未找到匹配的键名，返回null或其他标识
}
function waf(data) {
            data = JSON.stringify(data)
            if (data.includes('outputFunctionName') || data.includes('escape') || data.includes('delimiter') || data.includes('localsName')) {
                return false;
            }else{
                return true;
            }
}
//设置http
var server = app.listen(8888,function () {
    var port = server.address().port
    console.log("http://127.0.0.1:%s", port)
});