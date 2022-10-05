var path = require('path')
var fs = require('fs');
var allImageFolders = [];
var allImages = [];

fs.readdir('./images/', { withFileTypes: true }, (error, files) => {
    const directoriesInDIrectory = files
        .filter((item) => item.isDirectory())
        .map((item) => item.name);
        for(var i of directoriesInDIrectory){
            allImageFolders.push('./Images/' + '/' + i + '/' + getDirectories('./images/'+i) + '/');
        }
        for(const folder in allImageFolders){
            fs.readdir(folder , (err, files) => {
                files.forEach(file => {
                  console.log(file);
                });
            });
        }
});
function getDirectories(path) {
    return fs.readdirSync(path).filter(function (file) {
      return fs.statSync(path+'/'+file).isDirectory();
    });
}

console.log("Nadim")