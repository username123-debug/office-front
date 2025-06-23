import { ref } from 'vue';
import axios from 'axios';

export function getNotices(){


const notices = ref([]);

const get = async() => {
    try{
        const res = await axios.get('');
        notices.value = res.data; 
    }catch(error){
        console.error(error);
    }
};

return{
    notices,
    get
};

}
