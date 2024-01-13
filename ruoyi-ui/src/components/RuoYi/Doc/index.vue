<template>
  <div>
     <svg-icon icon-class="question-line" @click="goto" />
  </div>
</template>

<script setup>
const url = ref('http://aidex.vip');

function goto() {
  window.open(url.value)
}
</script>