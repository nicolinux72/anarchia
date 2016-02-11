# installo node utilizzando nvm, il porting di Ruby Version Manager
# per usare poi node, una volta effettuato il login digitare
# nvm use v0.12.2

wget -qO- https://raw.githubusercontent.com/creationix/nvm/v0.25.3/install.sh | bash

#evito di dover rilanciare la bash
source /home/vagrant/.nvm/nvm.sh

nvm install  v0.12.2

#installo i comandi globalmente
echo Attendere il completamento dello script
npm install  grunt-cli@~0.1.13 -g
npm install  bower@~1.3.12 -g
#npm install  bower-installer@~1.2.0 -g
#npm install  grunt@~0.4.5 -g
#npm install  grunt-autoprefixer@~2.2.0 -g
#npm install  grunt-bower-task@~0.4.0 -g
#npm install  grunt-contrib-clean@~0.6.0 -g
#npm install  grunt-contrib-copy@~0.7.0 -g
#npm install  grunt-contrib-jshint@~0.6.4 -g
#npm install  grunt-contrib-uglify@~0.7.0 -g
#npm install  grunt-karma@~0.7.0 -g
#npm install  jit-grunt@~0.9.0 -g
#npm install  karma@~0.12.0 -g
#npm install  karma-jasmine@~0.1.5 -g
#npm install  karma-phantomjs-launcher@~0.1.1 -g
#npm install  load-grunt-config@^0.17.1 -g
#npm install  time-grunt@~1.0.0 -g
#npm install  grunt-newer@^1.1.0 -g
