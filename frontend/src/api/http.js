import axios from 'axios'

const http = axios.create({
  baseURL: '/api',
  withCredentials: true,
})

function getCookie(name) {
  const m = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'))
  return m ? decodeURIComponent(m[2]) : ''
}

http.interceptors.request.use((config) => {
  if (!['get', 'head', 'options'].includes((config.method || 'get').toLowerCase())) {
    config.headers['X-CSRF-Token'] = getCookie('XSRF-TOKEN')
  }
  return config
})

export default http
