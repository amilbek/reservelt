<template>
  <div
    class="modal"
    v-if="showModal"
    style="display: block"
    @click.self="cancel"
  >
    <div class="modal-content">
      <h4>Are you sure?</h4>
      <p>
        Do you really want to delete your account? This action cannot be undone.
      </p>
      <button @click="$emit('confirm')" class="btn btn-danger">
        Yes, Delete
      </button>
      <button @click="$emit('cancel')" class="btn btn-secondary">Cancel</button>
    </div>
  </div>
</template>

<script>
  export default {
    props: {
      showModal: {
        type: Boolean,
        required: true,
      },
    },
    emits: ['confirm', 'cancel'],
  };
</script>

<style>
  body {
    font-family: Arial, sans-serif;
    margin: 0;
    padding: 0;
    box-sizing: border-box;
  }
  .modal {
    display: none;
    position: fixed;
    z-index: 1000;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    overflow: auto;
    background-color: rgba(0, 0, 0, 0.5);
  }

  .modal-content {
    background-color: #fefefe;
    margin: 15% auto;
    padding: 20px;
    border: 1px solid #888;
    width: 80%;
    max-width: 500px;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    text-align: center;
  }

  .btn {
    margin: 5px;
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }

  .btn-danger {
    background-color: red;
    color: white;
  }

  .btn-secondary {
    background-color: gray;
    color: white;
  }

  .btn-danger:hover,
  .btn-secondary:hover {
    opacity: 0.8;
  }
</style>
