<template>
  <div id="app">
    <router-view/>    
  </div>  
</template>

<script>
export default {
  name: 'App',
}
</script>

<style>
  #app {
    width: 100%;
    padding: 0 9px;
    margin: -10px  ;
    background-image: url(assets/14.jpg);
    background-size: cover;
    background-repeat: no-repeat;
    background-attachment: fixed;
  }

</style>
