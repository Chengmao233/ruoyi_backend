<template>
  <div>
    <svg-icon icon-class="github-line" @click="goto" />
  </div>
</template>

<script setup>
const url = ref('https://gitee.com/big-hedgehog/aidex-sharp');

function goto() {
  window.open(url.value)
}
</script>
