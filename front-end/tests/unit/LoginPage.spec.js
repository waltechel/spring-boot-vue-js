import Vue from 'vue'
import { shallowMount } from '@vue/test-utils'
import LoginPage from '@/views/LoginPage.vue'

describe('LoginPage.vue', () => {
  // it(name, fn, timeout) 함수는 jest API인 test(name, fn, timeout)의 별명이다.
  it('should render correct contents', () => {
    const Constructor = Vue.extend(LoginPage)
    const vm = new Constructor().$mount()
    expect(vm.$el.querySelector('h1').textContent).toEqual('TaskAgile')
  })
})
