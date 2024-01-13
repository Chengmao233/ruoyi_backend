<template>
  <div id="app" :class="size">
    <router-view />
  </div>
</template>
<script setup >
  // import store from '@/store'
  // const size = store.getters.size
  import useAppStore from "@/store/modules/app";
  const appStore = useAppStore();
  const size = computed(() => appStore.size);
</script>