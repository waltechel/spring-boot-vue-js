module.exports = {
  devServer: {
    port: 3000,
    proxy: {
      '/api/*': {
        target: 'http://localhost:8080'
      }
    }
  },
  // 진입점을 설정하는 옵션
  configureWebpack: {
    entry: {
      app: './src/main.js',
      style: [
        'bootstrap/dist/css/bootstrap.min.css'
      ]
    }
  }
};
